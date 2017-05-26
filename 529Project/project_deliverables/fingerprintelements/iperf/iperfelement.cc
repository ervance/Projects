#include <click/config.h>

#include <clicknet/ip.h>
#include <clicknet/tcp.h>

#include "iperfelement.hh"
CLICK_DECLS

IperfElement::IperfElement()
{
  this->map = new HashTable<IPAddress, phase_t>();
}

IperfElement::~IperfElement()
{
  delete this->map;
}

void IperfElement::push(int, Packet *p)
{
  const unsigned char *d = p->data() + 14, *body;
  const click_ip *ip = reinterpret_cast<const click_ip *>(d);
  const unsigned iph = ip->ip_hl * 4;
  const IPAddress src = IPAddress(ip->ip_src), dst = IPAddress(ip->ip_dst);

  if (this->map->get(src) == PHASE_CONFIRMED
	  || this->map->get(dst) == PHASE_CONFIRMED) {
	return output(1).push(p);
  }

  if (ip->ip_p == IP_PROTO_TCP) {
	const click_tcp *tcp = reinterpret_cast<const click_tcp *>(d + iph);
	const unsigned off = iph + tcp->th_off * 4;
	const unsigned len = p->length() - 14 - off;

	if (len > 0) {
	  body = d + off;
	  if (len == 37 && body[len - 1] == 0)
		this->map->set(dst, PHASE_SUSPICIOUS);
	  else if (this->map->get(src) == PHASE_SUSPICIOUS && body[len - 1] == 0x9)
		this->map->set(src, PHASE_SUSPECTED);
	  else if (this->map->get(dst) == PHASE_SUSPECTED
			   && (body[len - 1] == 0x52 || body[len - 1] == 0x64))
		this->map->set(dst, PHASE_CONFIRMED);
	}
  }

  output(0).push(p);
}

CLICK_ENDDECLS
EXPORT_ELEMENT(IperfElement)
