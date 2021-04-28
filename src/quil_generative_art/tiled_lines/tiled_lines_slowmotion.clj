(ns quil-generative-art.tiled-lines.tiled-lines
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.test.check.generators :as gen]))

(def width 500)
(def height 500)

(def patterns [nil :repeating])

(defn setup []
  {:seed (gen/generate gen/small-integer)
   :pattern 1
   :step 25
   :x 0
   :y 0})

(defn update-state [{:keys [key-pressed pattern x y] :as state}]
  (if (= (:x state) width)
    state
    (-> state
        (update :x (fn [x n]
                     (if (and (< (:x state) width) (>= (:y state) height))
                       (min width (+ x n))
                       x)) (:step state))
        (update :y (fn [y n]
                     (if (< (:y state) height)
                       (min height (+ y n))
                       0)) (:step state)))))

;; add a mode to draw it really slow, so that all the iterations can be seen
;; displayed the parameters
;; maybe with a key to increase or decrease the speed

(defn draw [{:keys [seed pattern key-pressed x y step] :as state}]
  ;(q/fill 255)
  ;(q/background 255) ;; clear screen
  (q/stroke-weight 3)
  (q/frame-rate 60)

  ;(println state)

  (case (get patterns pattern)
            ;; when the seed repeats at a certain frequency such as when using (+ seed x y)
            ;; an interesting pattern appears
    :repeating
    (if (gen/generate gen/boolean step (+ seed x y))
      (q/line x y (+ x step) (+ y step))
      (q/line (+ x step) y x (+ y step)))

    (if (gen/generate gen/boolean step (+ seed (* x 100) y))
      (q/line x y (+ x step) (+ y step))
      (q/line (+ x step) y x (+ y step))))

  #_(let [step 25
          pattern (get patterns pattern)]
      (case pattern
        :a :a
        (doseq [x (range 0 width step)]
          (doseq [y (range 0 height step)]
            (case pattern
            ;; when the seed repeats at a certain frequency such as when using (+ seed x y)
            ;; an interesting pattern appears
              :repeating
              (if (gen/generate gen/boolean step (+ seed x y))
                (q/line x y (+ x step) (+ y step))
                (q/line (+ x step) y x (+ y step)))

              (if (gen/generate gen/boolean step (+ seed (* x 100) y))
                (q/line x y (+ x step) (+ y step))
                (q/line (+ x step) y x (+ y step))))))))

  (case (and (q/key-pressed?) (q/key-as-keyword))
    :r (reset! (q/state-atom) (assoc (setup) :pattern pattern))
    :s (q/save (str "./images/tiled-lines" "-" (name (or (get patterns pattern) "default")) "-" (quot (System/currentTimeMillis) 1000) ".png"))

    :p
    (swap! (q/state-atom) assoc :pattern (condp = (inc pattern) (count patterns) 0 (inc pattern)))

    :+
    :increase-step

    :-
    :decrease-step

    :q
    "display keys and state on screen"
    nil))

(defn key-pressed [state event]
  (println "key pressed " event state)
  state)

(defn key-released [state event]
  (println "key released" state event)
  state)

