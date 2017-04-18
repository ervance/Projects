#lang racket
(define a '(1 2 3 4 5 6 7))

(define length (lambda (lst)
                 (if (null? lst)
                     0
                     (+ 1 (length (cdr lst)))
                     )
                 )
  )

(define isamember (lambda (x lst)
                    (cond
                      ((null? lst)
                      #f
                      )
                      ((equal? x (car lst))
                              #t)
                      (else
                       (isamember x (cdr lst)))
                      )
                    )
  )

(define sum
  (lambda (lst)
              (if (null? lst)
                  0
                  (+ (car lst)(sum (cdr lst))
                     )
                  )
              )
  )

(define addone
  (lambda (lst)
    (if (null? lst)
        '()
      (cons (+ 1 (car lst))(addone (cdr lst)))
      )
    )
  )

(define addx
  (lambda (x lst)
    (if (null? lst)
        '()
        (cons (+ x (car lst))(addx x (cdr lst)))
        )
    )
  )

(define last
  (lambda (lst1 list2)
    (append lst1 (list list2))
    )
  )
