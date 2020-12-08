(ns rlserver.generate.npc
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn generate-bat [state pos]
  (create-entity state
                 {:pos      pos
                  :hp       [1 1]
                  :animated 2}
                 [:npc :blocking]))

(defn generate-skeleton [state pos]
  (create-entity state
                 {:pos      pos
                  :hp       [3 3]
                  :animated 3}
                 [:npc :blocking]))
