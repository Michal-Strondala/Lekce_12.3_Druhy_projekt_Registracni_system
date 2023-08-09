document.addEventListener("DOMContentLoaded", function() {

    // Vytvoření uživatele
    const createUserButton = document.getElementById("createUserButton");
    createUserButton.addEventListener("click", createUser);

    function createUser() {
        const name = document.getElementById("name").value;
        const surname = document.getElementById("surname").value;

        const user = {
            name: name,
            surname: surname
        };

        fetch(`/api/v1/user`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("User creation failed");
            }
        })
        .then(data => {
            const resultDiv = document.getElementById("resultOfCreation");
            resultDiv.innerText = data;
        })
        .catch(error => {
            console.error("Error", error);
            const resultDiv = document.getElementById("resultOfCreation");
            resultDiv.innerText = "User creation failed";
        });

    }

    // Smazání uživatele
    const deleteUserButton = document.getElementById("deleteUserButton");
    deleteUserButton.addEventListener("click", deleteUser);

    function deleteUser() {
        const deleteUserIDInput = document.getElementById("deleteUserID");
        const deleteUserID = deleteUserIDInput.value;

        const user = {
                    deleteUserID: deleteUserID,
                };

        fetch(`api/v1/user/${deleteUserID}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("User deletion failed");
            }
        })
        .then(data => {
            const resultDiv = document.getElementById("resultOfDeletion");
            resultDiv.innerText = data;
        })
        .catch(error => {
            console.error("Error", error);
            const resultDiv = document.getElementById("resultOfDeletion");
            resultDiv.innerText = `User deletion failed. Cannot find user with ID = ${deleteUserID}`;
        });

    }

    // Úpravy uživatele
    const updateUserButton = document.getElementById("updateUserButton");
    updateUserButton.addEventListener("click", updateUser);

    function updateUser() {
        const updateUserWithIDInput = document.getElementById("updateUserWithID");
        const updateUserWithID = updateUserWithIDInput.value;

        const name = document.getElementById("newName").value;
        const surname = document.getElementById("newSurname").value;

        const user = {
            name: name,
            surname: surname
        };

        fetch(`/api/v1/user/${updateUserWithID}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("User update failed");
            }
        })
        .then(data => {
            const resultDiv = document.getElementById("resultOfUpdate");
            resultDiv.innerText = data;
        })
        .catch(error => {
            console.error("Error", error);
            const resultDiv = document.getElementById("resultOfUpdate");
            resultDiv.innerText = `User update failed. Cannot find user with ID = ${updateUserWithID}`;
        });

    }

    // Zobrazení informací o uživateli
    const showUserButton = document.getElementById("showUserButton");
    showUserButton.addEventListener("click", showUserByID);

    const showUserButtonWithDetail = document.getElementById("showUserButtonWithDetail");
    showUserButtonWithDetail.addEventListener("click", showUserByIDWithDetail);

    function showUserByID() {
        const showUserByIDInput = document.getElementById("showUserByID");
        const showUserByID = showUserByIDInput.value;

        fetch(`api/v1/user/${showUserByID}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("User not found");
            }
        })
        .then(data => {
            const resultDiv = document.getElementById("resultOfShowUserByID");
            resultDiv.innerText = JSON.stringify(data, null, 2);
        })
        .catch(error => {
            console.error("Error", error);
            const resultDiv = document.getElementById("resultOfShowUserByID");
            resultDiv.innerText = `Cannot find user with ID = ${showUserByID}`;
        });

    }

    function showUserByIDWithDetail() {
        const showUserByIDInput = document.getElementById("showUserByID");
        const showUserByID = showUserByIDInput.value;

        fetch(`api/v1/user/${showUserByID}?detail=true`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("User not found");
            }
        })
        .then(data => {
            const resultDiv = document.getElementById("resultOfShowUserByID");
            resultDiv.innerText = JSON.stringify(data, null, 2);
        })
        .catch(error => {
            console.error("Error", error);
            const resultDiv = document.getElementById("resultOfShowUserByID");
            resultDiv.innerText = "User not found";
        });

    }

    // Zobrazení informací o všech uživatelích
    const showUsersButton = document.getElementById("showUsersButton");
    showUsersButton.addEventListener("click", showUsers);

    const showUsersButtonWithDetail = document.getElementById("showUsersButtonWithDetail");
    showUsersButtonWithDetail.addEventListener("click", showUsersWithDetail);

    function showUsers() {
        fetch(`api/v1/users`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("No users found");
            }
        })
        .then(data => {
            const resultDiv = document.getElementById("resultOfShowUsers");
            resultDiv.innerText = JSON.stringify(data, null, 2);
        })
        .catch(error => {
            console.error("Error", error);
            const resultDiv = document.getElementById("resultOfShowUsers");
            resultDiv.innerText = "No users found";
        });

    }

    function showUsersWithDetail() {
        fetch(`api/v1/users?detail=true`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("No users found");
            }
        })
        .then(data => {
            const resultDiv = document.getElementById("resultOfShowUsers");
            resultDiv.innerText = JSON.stringify(data, null, 2);
        })
        .catch(error => {
            console.error("Error", error);
            const resultDiv = document.getElementById("resultOfShowUsers");
            resultDiv.innerText = "No users found";
        });

    }

});