(ns rlserver.generate.npc
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn generate-bat [state pos]
  (create-entity state
                 {:pos      pos
                  :hp       [1 1]
                  :animated 2}
                 [:ai :blocking]))

(defn generate-skeleton [state pos]
  (create-entity state
                 {:pos      pos
                  :hp       [2 2]
                  :animated 3}
                 [:ai :blocking :hostile]))
