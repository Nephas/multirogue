(ns rlserver.entity.entity
  (:require [rlserver.entity.state :refer [seq-store]]
            [rllib.state :refer [apply-seq]]
            [rlserver.entity.state :as s]))

(defn new-id []
  (swap! seq-store inc))

(defn create-flags [state id flags]
  (apply-seq state flags
             (fn [s e] (update s e conj id))))

(defn create-components [state id components]
  (apply-seq state components
             (fn [s [kind data]] (assoc-in s [kind id] data))))

(defn create-entity
  ([state comps flags id]
   (-> state
       (create-flags id flags)
       (create-components id comps)))
  ([state comps flags]
   (create-entity state comps flags (new-id)))
  ([state comps]
   (create-entity state comps [])))



(defn destroy-components [state id]
  (apply-seq state (keys s/COMPONENTS)
             (fn [s c] (update s c dissoc id))))

(defn destroy-flags [state id]
  (apply-seq state (keys s/FLAGS)
             (fn [s f] (update s f #(remove #{id} %)))))

(defn destroy-entity [state id]
  (-> state
      (destroy-components id)
      (destroy-flags id)))

(defn copy-entity [target source id]
  (-> target
      (apply-seq (keys s/COMPONENTS)
                 (fn [s c] (let [val (get-in source [c id])]
                             (if (some? val)
                               (assoc-in s [c id] val) s))))
      (apply-seq (keys s/FLAGS)
                 (fn [s f] (if (contains? (set (get source f)) id)
                             (update s f conj id) s)))))