#lang racket
(define eq1
  (* 1001 (/ (+ 300 5)(- (* 4 12) 70)))
  )

(define eq2
  (+ -12 (/ (sqrt (- (* 12 12)(* 4 2 5))) (* 2 2)))
  )

(define mylist '(2 4 (6 8) 9 7))

(define removeDups
  (lambda (lst1 lst2)
    (filter
     (lambda (x)
       (andmap (lambda (y)
                 (not (equal? x y)))
               lst2)
        ) lst1)
    )
  )

(define lst11 '(1 2 5 6 9 8 2))
(define lst12 '(3 1 8 2))



;(define intersection
  ;(lambda (lst1 lst2)
    ;(

(define intersection
  (lambda (lst1 lst2)
    (filter [lambda (x)
              (ormap [lambda (z)
                       (equal? x z)
                       ]
                     lst2)
              ] lst1)
    )
  )

(define palendrome?
  (lambda (string)
    (let*
        ([str (string->list string)]
        [pal (reverse (string->list string))])
    (equal? str pal)
      )
    )
  )