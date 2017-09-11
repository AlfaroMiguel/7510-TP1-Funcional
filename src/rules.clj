(ns rules
  (:use [facts :only [evaluate-fact replace-args]])
  )

(defn is-valid-rule?
  "Given a rule checks if it's valid"
  [rule]
  (not (= nil (re-matches #"^[a-z]+\(.+\) :- ([a-z]+\(.+\))+" rule))))

(defn rule-name
  "Given a rule, returns his name"
  [rule]
  (subs rule 0 (clojure.string/index-of rule "(")))

(defn exist-rule
  "Given a database and a query, checks whether the query corresponds to the name of a rule in the database"
  [database query]
  (let [rules-names (map rule-name (:rules database))]
    (.contains rules-names (rule-name query))
    ))

(defn get-rule-args
  "Receives a rule returns a list of his arguments"
  [rule]
  (map clojure.string/trim
       (clojure.string/split (subs rule (inc (clojure.string/index-of rule "(")) (clojure.string/index-of rule ")")) #","))
  )

(defn get-rule-facts
  "Receives a rule returns a list of his facts"
  [rule]
  (clojure.string/split (clojure.string/replace (subs rule (+ 3 (clojure.string/index-of rule ":- "))) #"\), " ");") #";"))

(defn get-generic-rule
  "Receives the database, a rule's name and returns a generic rule from the database"
  [database rule]
  (nth
   (filter (fn[x] ( = (rule-name x) (rule-name rule)))(:rules database))
   0
   )
  )

(defn evaluate-rule
  "It receives a database and a rule, evaluates all the facts that make up the rule and returns the result"
  [database rule]
  (let [gen-rule (get-generic-rule database rule)
        gen-facts (get-rule-facts gen-rule)
        facts-to-evaluate (replace-args gen-facts (zipmap (get-rule-args gen-rule) (get-rule-args rule)))
        facts-evaluated  (map (fn [x] (evaluate-fact database x)) facts-to-evaluate)]
    (reduce (fn[x y] (and (true? x) (true? y))) (seq facts-evaluated))
    )
  )
