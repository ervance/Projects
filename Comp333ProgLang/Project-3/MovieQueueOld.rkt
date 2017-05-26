#lang racket
;Test Data
(define qlst11 '( ("The Big Short" 1) ("Spotlight" 2)  ("Room" 3) ("Zootopia" 4) ) )
(define qlst12 '( ("The Lady in the Van" 4) ("The Witch" 2)  ("Hail, Caesar" 3) ("Deadpool" 1) ("Brooklyn" 5) ) )
;(define movie '(("Deadpool" 1)))


;returns name of movie m
(define name
  (lambda (m)
    (car m)
    )
  )
;returns priority of movie m
(define priority
  (lambda (m)
    (car (cdr m))
    )
  )

;compare the two movies m1 and m2
;returns true if m1 has lower priority
;than m2.  Otherwise it returns false
(define compare
  (lambda (m1 m2)
    (< (priority m1)(priority m2))
    )
  )

;prints the movie in form "Movie:Priority"
(define printMovie
  (lambda (m)
    (begin
      (display (car m))
      (display ":")
      (displayln (car (cdr m)))
       
      )
    )
  )

;prints movie queue using printMovie
;prints 1 per line
(define printQueue
  (lambda (qlst)
    (map printMovie (sort qlst compare)
         )
    )
  )

;Checks if s is the name of a movie in the qlst
(define memberMQ?
  (lambda (s qlst)
    (cond
      ((null? qlst) #f)
      ((equal? s (car (car qlst))) #t)
      (else (memberMQ? s (cdr qlst)))
      )
    )
  )

;returns priority of the movie s in qlst.
;returns #f if movie is not in qlst
(define getPriority
  (lambda (s qlst)
    (cond
      ((null? qlst) #f)
      ((equal? s (car (car qlst))) (car (cdr (car qlst))))
      (else (getPriority s (cdr qlst)))
      )
    )
  )

;returns name of movie in qlst with priority k
;returns #f if k is out of range
(define getName
  (lambda (k qlst)
    (cond
      ((null? qlst) #f)
      ((equal? k (car (cdr (car qlst)))) (car (car qlst)))
      (else (getName k (cdr qlst)))
      )
    )
  )
  

;removes the movie with priority k and updates the
;priorities of the movies on the list to reflect
;that the kth priority movie has been removed.
;Returns the updates list. If no movie has priority k,
;just return qlst
(define removeByPriority
  (lambda (k qlst)
    (sort (delPriority k
              (filter (lambda  (x)
                             (not (equal? k (car (cdr x)))
                                  )
                             )
                      qlst)
             )
        compare)
   )
  )

;helper function to update priority
(define delPriority
  (lambda (k lst)
    (map (lambda (x)
                    (cons (name x) (list (if (>= (priority x) k)
                                       (- (priority x) 1)
                                       (priority x)
                                       )))) lst)
    )
  )



;removes the movie with name s and updates the
;priorities of the movies on the list to reflect
;that the kth priority movie has been removed.
;Returns the updates list. If s is not in the
;list, just return qlst
(define removeByName
  (lambda (s qlst)
    (sort (delPriority (getPriority s qlst)
              (filter (lambda  (x)
                             (not (equal? s (car x))
                                  )
                             )
                      qlst)
             )
        compare)
   )
  )

;adds a movie with name s to qlst. It gives this movie the last priority.
(define addMQ
  (lambda (s qlst)
    (sort
     (cons
      (cons s (list (+ (length qlst) 1)))
      qlst)
     compare)
    )
  )


;helper to add in a movie with priority
(define insertPriority
  (lambda (k lst)
    (map (lambda (x)
         (cons (name x) (list (if (>= (priority x) k)
                                       (+ (priority x) 1)
                                       (priority x)
                                       )))) lst)
    )
  )

;adds a movie  (list s k ) to qlist  and updates the priorities
;of the other movies on the list. If the priority is out of
;range then give the movie the last priority.
(define insertMQ
  (lambda (s k qlst)
    (sort  (cons
                 (cons s (list k))
                              (insertPriority k qlst))
          compare)
    )
  )

;changes the priority of movie with name s to priority q.
;Updates qlst to reflect new priorities. If s is not in
;qlst or if k is out of range, return qlst.
(define updatePriority
  (lambda (s k qlst)
    (if (memberMQ? s qlst)
        (if (<= k (length qlst))
            (insertMQ s k (removeByName s qlst))
            (sort qlst compare)
            )
        (sort qlst compare)
        )
    )
  )

(define validMQ
  (lambda (z)
    (listAllTrue (map
                  (lambda (x)
                    (if (pair? x)
                        (if (string? (car x))
                            (if (number? (car (cdr x)))
                                #t
                                #f
                                )
                            #f
                            )
                        #f
                        )
                    )
                  z)
                 )
    )
  )

;true helper
(define listAllTrue
  (lambda (lst)
    (cond
      ((empty? lst) #t)
      ((car lst) (listAllTrue (cdr lst)))
      (else #f)
      )
    )
  )

; queue lists
(define qlst1 '(("The Big Short" 1)("Spotlight" 2)("Room" 3)("Zootopia" 4)))
(define qlst2 '(("The Lady in the Van" 4)("The Witch" 2)("Hail, Ceasar" 3)("Deadpool" 1)("Brooklyn" 5)))
(define qlst3 '(("Creed" 1)("Revenant" 2)))
(define t '("Moodlight" 3))
(define mqueue '())

(define test1
  (lambda ()
    (begin
      (displayln (priority t))
      (printQueue qlst2)
      (displayln (memberMQ? "The Witch" qlst2))
      (displayln (getPriority "Room" qlst1))
      (displayln (getName 1 qlst2)) ; displays #<void>
      (displayln (sort (removeByPriority 2 qlst2) compare)) ;removes movie but doesn't reset priorities
      (displayln (sort (removeByName "Spotlight" qlst1) compare)) 
      (displayln qlst1)
      (displayln (sort (addMQ "Only Yesterday" qlst2) compare))
      (displayln qlst2)
      (displayln (sort (addMQ "Star Wars" qlst1) compare))
      (displayln (sort (insertMQ "Triple 9" 2 qlst2) compare)) 
      (displayln (sort (updatePriority "The Lady in the Van" 5 qlst2) compare)) 
      (displayln (removeByPriority 1 (addMQ "Big Short" (addMQ "The Martian" qlst3)))) 
      (displayln (insertMQ "Knight of Cups" 1 (addMQ "Eddie the Eagle" mqueue)))
      (displayln (validMQ qlst1))
      (displayln (validMQ (list t)))
      (displayln (validMQ t))
      (displayln (validMQ '()))
      
     )))