let isCelsius = true;  // Toggle for Celsius/Fahrenheit
let isCm = true;       // Toggle for cm/inches

// Function to fetch data from the backend and update the frontend
function fetchFanData() {
    fetch('/api/fan-status')  // Assuming this is the backend API that returns the fan data
        .then(response => response.json())
        .then(data => {
            // Display temperature
            let temp = isCelsius ? data.temperature : (data.temperature * 9/5) + 32;
            document.getElementById("temperature").textContent = `Temperature: ${temp.toFixed(2)} ${isCelsius ? '°C' : '°F'}`;

            // Display distance
            let distance = isCm ? data.distanceCm : data.distanceInch;
            document.getElementById("distance").textContent = `Distance: ${distance.toFixed(2)} ${isCm ? 'cm' : 'inches'}`;

            // Display current
            document.getElementById("currentReading").textContent = `Current Consumption: ${data.current.toFixed(2)} A`;

            // Display electric usage
            document.getElementById("electricUsage").textContent = `Electric Usage: ${data.electricUsageKWh.toFixed(4)} kWh`;
        })
        .catch(error => console.error('Error fetching data:', error));
}

// Function to toggle between Celsius/Fahrenheit
function toggleTemperatureUnit() {
    isCelsius = !isCelsius;
    fetchFanData();
}

// Function to toggle between cm/inches
function toggleDistanceUnit() {
    isCm = !isCm;
    fetchFanData();
}

// Initial data fetch
fetchFanData();

// Set interval to fetch data every second
setInterval(fetchFanData, 1000);
