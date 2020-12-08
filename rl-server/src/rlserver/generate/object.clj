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

(defn generate-stair-up [state pos]
  (create-entity state
                 {:pos    pos
                  :sprite [21 1]}))

(defn generate-stair-down [state pos]
  (create-entity state
                 {:pos    pos
                  :sprite [22 1]}))