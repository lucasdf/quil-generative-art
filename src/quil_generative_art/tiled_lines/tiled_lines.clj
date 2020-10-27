(ns quil-generative-art.tiled-lines.tiled-lines
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.test.check.generators :as gen]))

(def width 500)
(def height 500)

(def patterns [nil :repeating])

(defn setup []
  {:seed (gen/generate gen/small-integer)
   :pattern 0})

(defn update-state [{:keys [key-pressed pattern] :as state}]
  state)

(defn draw [{:keys [seed pattern key-pressed]}]
  ;(q/fill 255)
  (q/background 255) ;; clear screen
  (q/stroke-weight 3)
  (let [step 25
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

