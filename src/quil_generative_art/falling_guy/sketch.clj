(ns quil-generative-art.falling-guy.sketch
  (:require [quil-generative-art.falling-guy.falling-guy :as falling-guy]
            [quil.core :as q]
            [quil.middleware :as m]))

(q/defsketch falling-guy
  :size [falling-guy/width falling-guy/height]
  :setup falling-guy/setup
  :update falling-guy/update-state
  :draw falling-guy/draw
  :middleware [m/fun-mode])

