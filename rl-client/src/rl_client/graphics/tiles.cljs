(ns rl-client.graphics.tiles
  (:require [quil.core :as q :include-macros true]))

(def COLS 32)
(def ROWS 24)
(def SIZE 8)
(def SCALE 2)

(def origin (. (. js/document -location) -origin))
(def tileset (atom nil))
(def tiles (atom nil))

(defn get-tile [x y]
  (nth @tiles (+ y (* x ROWS))))

(defn draw-at [img [x y]]
  (q/image img (* SCALE SIZE x) (* SCALE SIZE y)))

(defn slice-tileset []
    (when (nil? @tiles)
      (print "slicing tiles")
      (reset! tiles
            (for [x (range COLS)
                  y (range ROWS)]
              (let [im (q/create-image SIZE SIZE :rgb)]
                (q/copy @tileset im
                        [(* x SIZE) (* y SIZE) SIZE SIZE]
                        [0 0 SIZE SIZE])
                (q/resize im (* SCALE SIZE) (* SCALE SIZE))
                im)))))

(defn fetch-tileset []
   (reset! tileset (q/load-image (str origin "/img/tileset.png"))))