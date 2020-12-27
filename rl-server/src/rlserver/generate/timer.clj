(ns rlserver.generate.timer
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn generate-animated-effect
  ([state pos animation timer]
   (create-entity state
                  {:pos      pos
                   :timer    timer
                   :animated animation}))
  ([state pos animation]
   (generate-animated-effect state pos animation 1)))

(defn generate-sprite-effect
  ([state pos sprite timer]
   (create-entity state
                  {:pos      pos
                   :timer    timer
                   :sprite sprite}))
  ([state pos sprite]
   (generate-sprite-effect state pos sprite 1)))

