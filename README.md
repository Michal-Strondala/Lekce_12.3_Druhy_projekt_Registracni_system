# projekt_Registracni_system
Druhý projekt pro Engeto Academy Java kurz. Registrační systém pro fiktivní společnost Genesis Resources

# Zadání projektu

1. Aplikace bude obsahovat REST rozhraní
2. Data se budou ukládat do databáze
3. Je požadované správné rozdělení kódu (controller, service atd.)
4. Důležité je také dbát na čistotu kódu
5. Odevzdaní musí obsahovat export z Postmana (kterým je testované API)
6. Odevzdání musí ještě obsahovat SQL příkazy, kterými byla vygenerována databáze

# Požadavky na API
 - založení nového uživatele
 - informace o uživateli
 - informace o všech uživatelích
 - upravit informace o uživateli
 - smazat uživatele

# Založení nového uživatele
- POST api/v1/user
- Informace o novém uživateli budou předány skrz objekt
{ name: string, surname: string, personID: string(12) }

- personID musí být ověřeno, že je validní.
- Dále je nutné ověřit, zda již uživatel s personID není v databázi.

# UUID - Generování
Pro každý nový záznam musí být vygenerovaný ještě UUID, což bude další jedinečný identifikátor uživatele. Na vygenerování můžeš použít například následující knihovnu (https://www.baeldung.com/java-uuid).

# Informace o uživateli
 - GET api/v1/user/{ID}
 - Request vrátí následující objekt:
{id: string, name: string, surname: string }

 - GET api/v1/users/{ID}?detail=true
 - Request vrátí rozšířený objekt:
{id: string, name: string, surname: string, personID: string , uuid: string }

# Informace o všech uživatelích
 - GET api/v1/users
 - Request vrátí následující objekt:
List {id: string, name: string, surname: string }

 - GET api/v1/users?detail=true
 - Request vrátí rozšířený objekt:
List {id: string, name: string, surname: string, personID: string , uuid: string }

# Upravení informací uživatele
 - PUT api/v1/user
 - 
# Smazání uživatele
 - DELETE api/v1/user/{ID}
 
# Doplňující možnosti
 - logovací framework
 - front-end
