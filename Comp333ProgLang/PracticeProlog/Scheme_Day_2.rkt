#lang racket
(define myteam '("tim" "john" "ann"))
(define cube (lambda (x) (* x x x)))
(define a 2)
(define b 5)
(define eval_poly (lambda (a b c x) (+ (* a x x) (* b x))))

(define mid (lambda (a b c)
              (- (+ a b c) (min a b c) (max a b c))))

(define midSort (lambda (a b c)
                       (car ( cdr (sort (list a b c) <=)))))
