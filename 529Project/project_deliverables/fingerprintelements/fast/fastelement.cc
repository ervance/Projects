#include <click/config.h>
#include <clicknet/ip.h>
#include <clicknet/udp.h>
#include <string.h>
//#include <iostream>
#include "fastelement.hh"
using namespace std;

CLICK_DECLS

FastElement::FastElement()
{
  this->b_TestInProgress = 0;
}

FastElement::~FastElement()
{
  
}

void FastElement::push(int, Packet *p)
{
  const unsigned char *d = p->data() + 14;
  char *bodyChars;

  const click_ip *ip = reinterpret_cast<const click_ip *>(d);
  const unsigned iph = ip->ip_hl * 4;
 

  if (ip->ip_p == IP_PROTO_UDP) {
	
	const click_udp *udp = reinterpret_cast<const click_udp *>(d + iph);
	
	if(udp->uh_dport == 0x3500 && udp->uh_ulen > 0) { // 53 is DNS port, which is 0x35, but reads 32-bit word
		
		const unsigned off = iph + 20; // 20 to get to DNS name
		bodyChars = (char*)(d + off);

		if (b_TestInProgress) {
			if(!!strstr(bodyChars, "ichnaea") && !!strstr(bodyChars, "netflix")) { // trying to find ichnaea.netflix.com
				b_TestInProgress = 0;
			}
		}
		else { // not in a fast.com test yet
			// check for fast.com in CNAME
		  	if(!!strstr(bodyChars, "fast") && !!strstr(bodyChars, "com")) { // trying to get fast.com or api.fast.com
				b_TestInProgress = 1;
			}
		}		
	}
  }
  
  if (b_TestInProgress) {
	return output(1).push(p); // test underway - don't shape!
  }
  else {
	return output(0).push(p); // not a packet we care about - shape it - shape it good
  }
}

CLICK_ENDDECLS
EXPORT_ELEMENT(FastElement)
