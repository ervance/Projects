#lang racket
;practice set 2

( define f
   ( lambda (lst)
      (let
          (
           [x (* (car (car lst)) (car (cadr lst)))]
           [y (* (cadr (car lst)) (cadr (cadr lst)))]
           )
        (+ x y)
        )
      )
   )

(define lst1 '((3 5)(6 9)))


;distanceFromOrgin
(define distanceFromOrigin
  (lambda (lst)
    (sqrt ( +
            (* (car lst) (car lst))
            (* (car (cdr lst)) (car (cdr lst)))
            )
          )
    )
  )

(define plst '(1 1))

;closestPoints
(define closestPoints
  (lambda (lst)
    (car (car
     (sort
      (map (lambda (points)
            (cons (distanceFromOrigin points) points)
            ) lst)
      (lambda (x y )
        ( < (car  x)(car y))
        )
     )
         )
    )
  )
  )

;sum even numbers in a list
(define sum
  (lambda (lst)
    (if (null? lst)
        0
        (+ (car lst)(sum (cdr lst)))
        )
    )
  )
(define sumEven
  (lambda (lst)
    (sum (filter even? lst))
    )
)

(define elst '( 1 2 3 4 5 6 7 8 9 10))

(define clst '((1 0.5)(1.1 0.4)(-1 0.6)))

;Remove duplicates from list
(define removeDups
  (lambda (lst1 lst2)
    (map (lambda (

    (cond
      ((or (null? lst1)(null?lst2)) '())
      (