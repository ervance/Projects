#ifndef CLICK_SPEEDTESTELEM_HH
#define CLICK_SPEEDTESTELEM_HH
#include <click/element.hh>
#include <click/hashtable.hh>
#include <click/ipaddress.hh>

CLICK_DECLS

struct suspicion_t{
    unsigned count_socket;
    unsigned count_pong;
    unsigned count_download;
    unsigned char confirmed;
    suspicion_t() {
      this->count_socket = this->count_pong = this->count_download = 0;
      this->confirmed = 0;
    }
    unsigned char operator== (const suspicion_t other) {
      return this->count_socket == other.count_socket
        && this->count_pong == other.count_pong
        && this->count_download == other.count_download;
    }
    unsigned char operator!= (const suspicion_t other) {
      return !this->operator==(other);
    }
  };

class SpeedTestElement : public Element {

  HashTable<IPAddress, suspicion_t> *map;

  public:

  SpeedTestElement() CLICK_COLD;
  ~SpeedTestElement();

  const char* class_name() const	{ return "SpeedTest"; }
  const char* port_count() const	{ return "1/2"; }

  Packet* pull(int){return input(0).pull();}
  void push(int, Packet *);

};

CLICK_ENDDECLS
#endif
