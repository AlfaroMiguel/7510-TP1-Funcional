(ns rules
  (:use [facts :only [evaluate-fact replace-args]])
  )

(defn is-valid-rule?
  "Chequea si es una rule valida usando regex"
  [rule]
  (not (= nil (re-matches #"^[a-z]+\(.+\) :- ([a-z]+\(.+\))+" rule))))

(defn rule-name
  "Devuelve el nombre de una rule"
  [rule]
  (subs rule 0 (clojure.string/index-of rule "(")))

(defn exist-rule
  "Chequea que sea el nombre de una rule devuelve true si es asi"
  [database query]
  (let [rules-names (map rule-name (:rules database))]
    (.contains rules-names (rule-name query))
    ))

(defn get-rule-args
  ""
  [rule]
  (map clojure.string/trim
       (clojure.string/split (subs rule (inc (clojure.string/index-of rule "(")) (clojure.string/index-of rule ")")) #","))
  )

(defn get-rule-facts
  ""
  [rule]
  (clojure.string/split (clojure.string/replace (subs rule (+ 3 (clojure.string/index-of rule ":- "))) #"\), " ");") #";"))

(defn get-generic-rule
  "Busca en la database la rule generica y la devuelve, como los nombres son unicos se que tiene un solo elemento"
  [database rule]
  (nth
   (filter (fn[x] ( = (rule-name x) (rule-name rule)))(:rules database))
   0
   )
  )

(defn evaluate-rule
  ""
  [database rule]
  (let [gen-rule (get-generic-rule database rule)
        gen-facts (get-rule-facts gen-rule)
        facts-to-evaluate (replace-args gen-facts (zipmap (get-rule-args gen-rule) (get-rule-args rule)))
        facts-evaluated  (map (fn [x] (evaluate-fact database x)) facts-to-evaluate)]
    (reduce (fn[x y] (and (true? x) (true? y))) (seq facts-evaluated))
    )
  )
