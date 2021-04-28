(ns quil-generative-art.tiled-lines.sketch
  (:require [quil-generative-art.tiled-lines.tiled-lines :as tiled-lines]
            [quil.core :as q]
            [quil.middleware :as m]))

(q/defsketch tiled-lines
  :size [tiled-lines/width tiled-lines/height]
  :setup tiled-lines/setup
  :update tiled-lines/update-state
  :draw tiled-lines/draw
  :key-pressed tiled-lines/key-pressed
  :key-released tiled-lines/key-released
  :middleware [m/fun-mode])

