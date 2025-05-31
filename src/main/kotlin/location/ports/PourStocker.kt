package location.ports

// A VOUS DE DESIGNER LA SIGNATURE DE CETTE INTERFACE


















// AUTRES IDEES

// dans une branche de développement, on va utiliser une interface
// interface  PourStocker<TEntity> where TEntity : Entity
// Entity doit avoir un ID

//  séparation des responsabilités entre query(read) et write => CQRS = CQS + ISP

// 2e marche: faire un 2e repo (+ adapter) pour une autre entité, qui justifie encore plus CQRS