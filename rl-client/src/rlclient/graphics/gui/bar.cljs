(ns rlclient.graphics.gui.bar
  (:require [rlclient.graphics.sheets :as sh]
            [rlclient.graphics.camera :as c]))

(defn bar [[x y] [now limit] tile]
  (let [empty (sh/get-tile [14 11])
        full (sh/get-tile tile)]
    (doseq [i (range limit)]
      (c/draw-fixed (if (< i now) full empty) [(+ i x) y]))))

(defn red-bar [pos resource]
  (bar pos resource [12 11]))

(defn green-bar [pos resource]
  (bar pos resource [13 11]))