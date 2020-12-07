(ns rlserver.generate.entity
  (:require [rlserver.state :refer [seq-store]]))

(defn new-id []
  (swap! seq-store inc))

(defn generate-entity [state id pos blocking?]
  (-> state
      (assoc :entities id)
      (assoc-in [:pos id] pos)
      (update :blocking (if blocking? #(conj % id) identity))))