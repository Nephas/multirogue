(ns rlserver.generate.object
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn generate-corpse [state pos]
  (create-entity state
                 {:pos    pos
                  :sprite [8 7]}))

(defn generate-blood [state pos]
  (create-entity state
                 {:pos    pos
                  :sprite [8 5]}))

(defn generate-potion [state pos]
  (create-entity state
                 {:pos        pos
                  :sprite     [17 8]
                  :consumable :restore}))

(defn generate-stair-up [state pos [fromlvl tolvl]]
  (create-entity state
                 {:pos        pos
                  :transition [fromlvl tolvl]
                  :sprite     [21 1]}))

(defn generate-stair-down [state pos [fromlvl tolvl]]
  (create-entity state
                 {:pos        pos
                  :transition [fromlvl tolvl]
                  :sprite     [22 1]}))

(defn generate-runestone [state pos]
  (create-entity state
                 {:pos    pos
                  :sprite (rand-nth [[8 10] [9 10] [10 10] [11 10]])}
                 [:block]))