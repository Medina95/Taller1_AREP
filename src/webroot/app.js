document.getElementById('fetchDataBtn').addEventListener('click', function() {
    fetch('/api/dinosaurios')
        .then(response => response.json())
        .then(data => {
            document.getElementById('dataOutput').textContent = JSON.stringify(data, null, 2);
        })
        .catch(error => console.error('Error:', error));
});

document.getElementById('addMessageBtn').addEventListener('click', function() {
    const Dinosaurio = document.getElementById('newMessage').value;

    fetch('/api/dinosaurio', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            Dinosaurio: Dinosaurio
        })
    })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            // Actualiza los datos después de agregar el mensaje
            document.getElementById('dataOutput').textContent = JSON.stringify(data, null, 2);
        })
        .catch(error => console.error('Error:', error));
});

document.getElementById('deleteMessageBtn').addEventListener('click', function() {
    const id = document.getElementById('deleteId').value;
    console.log('Deleting ID:', id);
    fetch(`/api/dinosaurio/${id}`, {
        method: 'DELETE',
        
    })
        .then(response => response.json())
        .then(data => {
            console.log('Deleted:', data);
            document.getElementById('dataOutput').textContent = JSON.stringify(data, null, 2);

        })
        .catch(error => console.error('Error:', error));
});

document.getElementById('updateMessageBtn').addEventListener('click', function() {
    const id = document.getElementById('updateId').value;
    const Dinosaurio = document.getElementById('NewDinosaurio').value;

    console.log('Deleting ID:', id);
    fetch(`/api/dinosaurio/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            Dinosaurio: Dinosaurio
        })
    })
        .then(response => response.json())
        .then(data => {
            console.log('UPdate:', data);
            document.getElementById('dataOutput').textContent = JSON.stringify(data, null, 2);

        })
        .catch(error => console.error('Error:', error));
});
