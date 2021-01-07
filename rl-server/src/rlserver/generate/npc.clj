(ns rlserver.generate.npc
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn generate-raven [state pos]
  (create-entity state
                 {:pos      pos
                  :desc     "raven"
                  :hp       [1 1]
                  :dmg      1
                  :animated 8}
                 [:ai :block]))

(defn generate-godi [state pos]
  (create-entity state
                 {:pos      pos
                  :desc     "godi"
                  :hp       [8 8]
                  :dmg      1
                  :animated 11}
                 [:ai :block]))

(defn generate-skeleton [state pos]
  (create-entity state
                 {:pos      pos
                  :desc     "draugr"
                  :hp       [2 2]
                  :dmg      1
                  :animated 5}
                 [:ai :block :hostile]))

(defn generate-wolf [state pos]
  (create-entity state
                 {:pos      pos
                  :desc     "wolf"
                  :hp       [4 4]
                  :dmg      3
                  :animated 4}
                 [:ai :block :hostile]))

(defn generate-snake [state pos]
  (create-entity state
                 {:pos      pos
                  :desc     "snake"
                  :hp       [2 2]
                  :dmg      2
                  :animated 10}
                 [:ai :block :hostile]))