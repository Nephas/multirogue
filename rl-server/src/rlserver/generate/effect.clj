(ns rlserver.generate.effect
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn generate-effect [state pos animation]
  (create-entity state
                 {:pos    pos
                  :effect animation}))
