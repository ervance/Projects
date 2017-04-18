/*Problem 1 - Family tree*/
female(claudeen).
female(helen).
female(elane).
female(joan).
female(brenda).
female(rebecca).
female(joyce).
female(patricia).
female(kathy).
female(melissa).

male(robert).
male(alan).
male(bill).
male(doug).
male(john).
male(lloyd-sr).
male(lloyd).
male(harley).
male(fred).
male(eric).
male(william).
male(garrett).
male(cory).
male(jacob).

parent(helen, elane).
parent(john, elane).
parent(claudeen, lloyd).
parent(lloyd-sr, lloyd).
parent(claudeen, bill).
parent(lloyd-sr, bill).
parent(joan, fred).
parent(harley, fred).
parent(joan, patricia).
parent(harley, patricia).
parent(joan, kathy).
parent(harley, kathy).
parent(elane, brenda).
parent(lloyd, brenda).
parent(elane, robert).
parent(lloyd, robert).
parent(elane, alan).
parent(lloyd, alan).
parent(fred, rebecca).
parent(fred, eric).
parent(brenda, rebecca).
parent(brenda, eric).
parent(robert, william).
parent(robert, garrett).
parent(patricia, cory).
parent(patricia, jacob).
parent(kathy, melissa).

ancestor(X, Y) :- parent(X, Y).
ancestor(X, Y) :- parent(X, Z), ancestor(Z, Y).
grandparent(X, Y) :- parent(X, Z), parent(Z, Y).
grandfather(X, Y) :- male(X), grandparent(X, Y).
grandmother(X, Y) :- female(X), grandparent(X, Y).
mother(X, Y) :- female(X), parent(X, Y).
father(X, Y) :- male(X), parent(X, Y).
aunt(X, Y) :- parent(Z, Y), sister(X,Z).
uncle(X, Y) :- parent(Z, Y), brother(X,Z).
firstCousin(X,Y) :- parent(Z,X), uncle(Z,Y).
firstCousin(X,Y) :- parent(Z,X), aunt(Z,Y).
sister(X, Y) :- female(X), parent(Z,X), parent(Z,Y), X\=Y.
brother(X, Y) :- male(X), parent(Z,X), parent(Z,Y), X\=Y.
decendant(X, Y) :- parent(Y, X).
decendant(X, Y) :- parent(Y, Z), decendant(X, Z).
relatives(X,Y) :- decendant(X,Y).
relatives(X,Y) :- decendant(X,Z), decendant(Y,Z), X\=Y.
relatives(X,Y) :- ancestor(X,Y).
relatives(X,Y) :- ancestor(Z,X), ancestor(Z,Y),X\=Y.




/*Problem 2 Grammar*/

letter(X) :- member(X, [a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z]).

e(X) :- append(List1, [+|List2],X), e(List1), t(List2).
e(X) :- append(List1, [-|List2],X), e(List1), t(List2).
e(X) :- t(X).
t(X) :- append(List1, [*|List2],X), t(List1), f(List2).
t(X) :- append(List1, [/|List2],X), t(List1), f(List2).
t(X) :- f(X).
f(X) :- append([ '(' | List ], [')'], X), e(List).
f([X]) :- letter(X).
f([X]) :- number(X).

