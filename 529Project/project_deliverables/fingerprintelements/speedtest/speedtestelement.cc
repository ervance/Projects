#include <click/config.h>
#include <clicknet/ip.h>
#include <clicknet/tcp.h>
#include <string.h>
#include "speedtestelement.hh"

CLICK_DECLS

SpeedTestElement::SpeedTestElement()
{
  this->map = new HashTable<IPAddress, suspicion_t>();
}

SpeedTestElement::~SpeedTestElement()
{
  delete this->map;
}

unsigned char count_checker(struct suspicion_t* record){
	return (record->count_socket == 5 && record->count_pong == 14);
}

void SpeedTestElement::push(int, Packet *p)
{
  const unsigned char *d = p->data() + 14;
  char *body;
  const click_ip *ip = reinterpret_cast<const click_ip *>(d);
  const unsigned iph = ip->ip_hl * 4;
  const IPAddress src = IPAddress(ip->ip_src),
	dst = IPAddress(ip->ip_dst), *key  = &src;

  suspicion_t record = this->map->get(src);

  if (record == this->map->default_value()) {
  	record = this->map->get(dst);
  	key = &dst;
  }

  if (record != this->map->default_value() && record.confirmed) {
  	return output(1).push(p);
  }

  if (ip->ip_p == IP_PROTO_TCP) {
	const click_tcp *tcp = reinterpret_cast<const click_tcp *>(d + iph);
	const unsigned off = iph + tcp->th_off * 4;
	const unsigned len = p->length() - 14 - off;

	unsigned char dirty = 0;

	if (len > 0) {
	  body = (char*)(d + off);
	  if((dirty = !!strstr(body, "HTTP/1.1 101 Switching Protocols"))){
	  	if (record == this->map->default_value()) {
  		  record = suspicion_t();
  		  key = &dst;
  		}
	  	++record.count_socket;
	  	record.confirmed = record.confirmed || count_checker(&record);
	  }else if((dirty = !!strstr(body, "PONG"))){
	  	++record.count_pong;
	  	record.confirmed = record.confirmed || count_checker(&record);
	  }

	  if(dirty){
	  	this->map->set(*key, record);
	  }
	}
  }

  output(0).push(p);
}

CLICK_ENDDECLS
EXPORT_ELEMENT(SpeedTestElement)
