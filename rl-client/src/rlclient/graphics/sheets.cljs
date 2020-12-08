(ns rlclient.graphics.sheets
  (:require [quil.core :as q :include-macros true]
            [rlclient.graphics.camera :refer [SIZE SCALE]]))

(def origin (. (. js/document -location) -origin))
(def tilesheet (atom nil))
(def animationsheet (atom nil))

(def tiles (atom nil))
(def animations (atom nil))

(defn get-tile
  ([x y] (nth @tiles (+ y (* x 24))))
  ([[x y]] (get-tile x y)))

(defn get-animation [n seed]
  (let [fr (mod (+ seed (q/frame-count)) 5)]
    (nth @animations (+ n (* fr 5)))))

(defn slice [sheet cols rows]
  (for [x (range cols)
        y (range rows)]
    (let [im (q/create-image SIZE SIZE :rgb)]
      (q/copy sheet im [(* x SIZE) (* y SIZE) SIZE SIZE] [0 0 SIZE SIZE])
      (q/resize im (* SCALE SIZE) (* SCALE SIZE))
      im)))

(defn slice-tileset []
  (when (nil? @tiles)
    (print "slicing tiles")
    (reset! tiles (slice @tilesheet 32 24)))
  (when (nil? @animations)
    (print "slicing animations")
    (reset! animations (slice @animationsheet 10 5))))

(defn fetch-tileset []
  (reset! tilesheet (q/load-image (str origin "/img/tileset.png"))))

(defn fetch-animations []
  (reset! animationsheet (q/load-image (str origin "/img/animations.png"))))