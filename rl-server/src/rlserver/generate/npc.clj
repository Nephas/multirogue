(ns rlserver.generate.npc
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn generate-raven [state pos]
  (create-entity state
                 {:pos      pos
                  :hp       [1 1]
                  :ap       [3 3]
                  :dmg      1
                  :animated 8}
                 [:ai :block]))

(defn generate-skeleton [state pos]
  (create-entity state
                 {:pos      pos
                  :hp       [4 4]
                  :ap       [4 4]
                  :dmg      3
                  :animated 5}
                 [:ai :block :hostile]))

(defn generate-wolf [state pos]
  (create-entity state
                 {:pos      pos
                  :hp       [6 6]
                  :ap       [6 6]
                  :dmg      3
                  :animated 4}
                 [:ai :block]))

(defn generate-snake [state pos]
  (create-entity state
                 {:pos      pos
                  :hp       [4 4]
                  :ap       [6 6]
                  :dmg      3
                  :animated 10}
                 [:ai :block :hostile]))