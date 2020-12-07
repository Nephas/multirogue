(ns rlserver.generate.npc
  (:require [rlserver.generate.entity :refer [generate-entity new-id]]))

(defn generate-npc [state pos]
  (let [id (new-id)]
    (-> state
        (generate-entity id pos true)
        (update :npc conj id)
        (assoc-in [:animated id] 2))))