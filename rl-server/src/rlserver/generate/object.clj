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
                 {:pos    pos
                  :sprite [17 8]
                  :consumable :restore}))

(defn generate-stair-up [state pos lvlid]
  (create-entity state
                 {:pos        pos
                  :transition lvlid
                  :sprite     [21 1]}))

(defn generate-stair-down [state pos lvlid]
  (create-entity state
                 {:pos        pos
                  :transition lvlid
                  :sprite     [22 1]}))