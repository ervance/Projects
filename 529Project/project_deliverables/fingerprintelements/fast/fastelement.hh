#ifndef CLICK_FASTELEMENT_HH
#define CLICK_FASTELEMENT_HH

#include <click/element.hh>
//#include <click/ipaddress.hh>
#include <netinet/in.h>

CLICK_DECLS

class FastElement : public Element { 

  unsigned b_TestInProgress;

  public:

  FastElement() CLICK_COLD;
  ~FastElement();

  const char* class_name() const	{ return "FastElement"; }
  const char* port_count() const	{ return "1/2"; }
  
  Packet* pull(int){return input(0).pull();}
  void push(int, Packet *);

};

CLICK_ENDDECLS
#endif
