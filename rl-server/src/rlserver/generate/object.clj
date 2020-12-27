(ns rlserver.generate.object
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn generate-corpse [state pos]
  (create-entity state
                 {:pos    pos
                  :desc   "bones"
                  :sprite [8 7]}))

(defn generate-blood [state pos]
  (create-entity state
                 {:pos    pos
                  :sprite [8 5]}))

(defn generate-potion [state pos]
  (create-entity state
                 {:pos        pos
                  :desc       "potion"
                  :sprite     [17 8]
                  :consumable :heal}))

(defn generate-stair-up [state pos [fromlvl tolvl]]
  (create-entity state
                 {:pos        pos
                  :desc       "stairs"
                  :transition [fromlvl tolvl]
                  :sprite     [21 1]}))

(defn generate-stair-down [state pos [fromlvl tolvl]]
  (create-entity state
                 {:pos        pos
                  :desc       "stairs"
                  :transition [fromlvl tolvl]
                  :sprite     [22 1]}))

(defn generate-goal [state pos fromlvl]
  (create-entity state
                 {:pos        pos
                  :transition [fromlvl 999]
                  :sprite     [15 1]}))

