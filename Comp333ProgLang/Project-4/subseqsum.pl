/*Given a list of integers L and an integer M, find subsequences of L whose sum is exactly M*/

subseq([],[]).
subseq([H|T],[H|R]) :- subseq(T,R).
subseq([_|T],R) :- subseq(T,R).

sum_list([],0).
sum_list([H|T], M) :- sum_list(T,N), M is H + N.


subseqSum(L,M,X) :- subseq(L,X), sum_list(X,M).


split(X,List,Split):- append(Split,[X|List],List).

fsm(2,[]).
