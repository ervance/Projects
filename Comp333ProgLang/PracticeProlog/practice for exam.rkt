#lang racket
(define addone
  (lambda (lst)
    (if (null? lst)
        lst
        (cons (cons (car(car lst)) (list (+ 1 (car (cdr (car lst)))))) (addone (cdr lst)))
          )
        )
    )

  

(define lst '((4 5)(9 10)(7 8)))

(define sum
  (lambda (x)
    (if (null? x)
        0
        (+ (car x)(sum (cdr x)))
        )
    )
  )