
fsm(3,[]).
fsm(0,[a|T]) :- fsm(1,T).
fsm(1,[a|T]) :- fsm(0,T).
fsm(2,[a|T]) :- fsm(0,T).
fsm(3,[a|T]) :- fsm(3,T).
fsm(0,[b|T]) :- fsm(0,T).
fsm(1,[b|T]) :- fsm(2,T).
fsm(2,[b|T]) :- fsm(3,T).
fsm(3,[b|T]) :- fsm(3,T).
