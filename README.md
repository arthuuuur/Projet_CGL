Pour la base de données:

I/ Via docker
  1/ Windows
- CMD : ``docker pull mariadb``
- CMD : ``docker run --name cgl-project -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mariadb``

  2/ Linux
- CMD : ``docker pull linuxserver/mariadb``
- CMD : ``docker run --name cgl-project -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d linuxserver/mariadb``

OU

- installez MariaDB (https://go.mariadb.com/download-mariadb-server-community109.html)


Pour l'application:

Dans la class SecurityConfiguration, nous avons définit 4 faux utilisateurs, ainsi que leurs roles.
Il faut se connecter avec l'un des ces utilisateurs pour pouvoir accéder à l'application.
Il n'est pas nécessaire de les créer dans la base de données. Cela est fait automatiquement à la première connexion.
Nous avons ajouté une méthode (fakeCheckUserLikeLDAP() ) qui simule une sorte de LDAP à la connexion.

- username: chuck.norris, password: admin, role: ADMIN, USER
- username: gaetan.vitrac, password: user, role: USER
- username: loick.vernet, password: user, role: USER
- username: arthur.troyon, password: user, role: USER

----------------------

- Un USER peut ajouter des affaires et modifier son propre profil, notamment pour modifier son parrain.
- Un ADMIN a aussi le role USER et peut quant à lui modifer le profil de tous les utilisateurs. Il peut aussi acceder
  aux parameters de l'application et aux statistiques.

----------------------

Au niveau des tests, nous avons fait des tests unitaires pour les entités, les controllers et les services.
Concernant les services, seules les méthodes métiers ont été testées.
Les méthodes concernant les datatables ont donc était ignorées.

Il y a aussi des tests e2e qui nécessitent d'avoir Chrome installé sur la machine.