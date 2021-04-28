(ns quil-generative-art.falling-guy.falling-guy
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.test.check.generators :as gen]))

(def width 500)
(def height 500)

(def patterns [nil :repeating])

(defn setup []
  {:head-x 20
   :head-y 0
   :speed 1})

(defn update-state [state]
  #_(-> state
        (update :x (fn [x]
                     x))
        (update :y (fn [y]
                     (min (inc y) (- height 6))))
        (update :arms (fn [x dir]
                        (if (= dir :down)
                          (dec x)
                          (inc x))) (:arms-direction state))
        ((fn [x] (cond
                   (> (:arms state) 10)
                   (assoc x :arms-direction :down)

                   (= (:arms state) -5)
                   (assoc x :arms-direction :up)

                   :else
                   x))))
  (-> state
      (update :speed + 0.007M)
      #_(update :head-x (fn [n] (if (< n (- width 10))
                                (inc n)
                                n)))
      #_(update :head-y (fn [n] (if (< n (- height 30))
                                (inc n)
                                n)))))

(def body-width 30)
(def leg-width 10)
(def arm-width 10)

(def body-length 25)
(def speed 5)
(def len 25)

(defn draw [{:keys [head-x head-y speed]}]
  ;(q/fill 255)
  (q/background 255) ;; clear screen
  (q/stroke-weight 3)

  


  (let [
        hip-x head-x
        hip-y (+ head-y body-length 6)
        a1 (q/sin speed)
        a2 (- (- (q/sin speed) 0.1) 0.3)
        a3 (+ (q/sin speed) speed)
        a4 (- (q/sin (+ speed -0.1)) 0)
        left-knee-x (+ hip-x (* (q/sin a1) len))
        left-knee-y (+ hip-y (* (q/cos a1) len))
        right-knee-x (+ hip-x (* (q/sin a3) len))
        right-knee-y (+ hip-y (* (q/cos a3) len))

        left-ankle-x (+ left-knee-x (* (q/sin a1) len))
        left-ankle-y (+ left-knee-y (* (q/cos a1) len))

        right-ankle-x (+ right-knee-x (* (* 0.5 (q/sin a4)) len))
        right-ankle-y (+ right-knee-y (* (q/cos a4) len))]
  (q/ellipse head-x head-y 12 12)

  ;; arms
  (q/line head-x (+ head-y 15) (+ head-x 35) head-y)
  (q/line head-x (+ head-y 15) (- head-x 35) head-y)
  ;; body
  (q/line head-x (+ head-y 8) hip-x hip-y)
  ;; legs
  (q/line hip-x hip-y left-knee-x left-knee-y)
  (q/line left-knee-x left-knee-y left-ankle-x left-ankle-y)
  (q/line hip-x hip-y right-knee-x right-knee-y)
  (q/line right-knee-x right-knee-y right-ankle-x right-ankle-y)

  ))


