#ifndef CLICK_IPERFELEMENT_HH
#define CLICK_IPERFELEMENT_HH

#include <click/element.hh>
#include <click/hashtable.hh>
#include <click/ipaddress.hh>
#include <netinet/in.h>

CLICK_DECLS

class IperfElement : public Element {
  typedef enum {
	PHASE_UNKNOWN = 0,
	PHASE_SUSPICIOUS,
	PHASE_SUSPECTED,
	PHASE_CONFIRMED,
	PHASE_TEARDOWN
  } phase_t;
  HashTable<IPAddress, phase_t> *map;

public:
  IperfElement() CLICK_COLD;
  ~IperfElement();

  const char* class_name() const { return "Iperf"; }
  const char* port_count() const { return "1/2"; }

  void push(int, Packet*);
  Packet* pull(int) { return input(0).pull(); }
};

CLICK_ENDDECLS
#endif
