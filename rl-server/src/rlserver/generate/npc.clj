(ns rlserver.generate.npc
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn generate-raven [state pos]
  (create-entity state
                 {:pos      pos
                  :hp       [1 1]
                  :ap       [3 3]
                  :dmg      1
                  :animated 6}
                 [:ai :blocking]))

(defn generate-skeleton [state pos]
  (create-entity state
                 {:pos      pos
                  :hp       [4 4]
                  :ap       [4 4]
                  :dmg      3
                  :animated 3}
                 [:ai :blocking :hostile]))
