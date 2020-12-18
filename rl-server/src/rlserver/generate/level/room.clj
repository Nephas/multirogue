(ns rlserver.generate.level.room
  (:require [rllib.rand :refer [rand-coll]]
            [rllib.board :refer [rect]]))

(defn rand-field [[[x y] w h]]
  (rand-coll (rect [x y] w h 2)))