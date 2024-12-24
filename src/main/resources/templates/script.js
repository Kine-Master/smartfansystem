document.addEventListener("DOMContentLoaded", () => {
    // Fetch and display the fan status from the server on page load
    fetchFanStatus();

    // Set up event listener for the threshold update button
    document.getElementById("update-thresholds").addEventListener("click", updateThresholds);
});

// Function to fetch fan status from the backend server
async function fetchFanStatus() {
    try {
        const response = await fetch("http://localhost:8080/api/fan-status"); // Make sure the backend is running
        if (response.ok) {
            const data = await response.json();
            updateFanStatusUI(data);
        } else {
            console.error("Error fetching fan status:", response.statusText);
            document.getElementById("status-text").textContent = "Error fetching status";
        }
    } catch (error) {
        console.error("Error fetching fan status:", error);
        document.getElementById("status-text").textContent = "Error fetching status";
    }
}

// Function to update the UI with fan status data
function updateFanStatusUI(data) {
    document.getElementById("temperature").textContent = `Temperature: ${data.temperature} °C`;
    document.getElementById("distance").textContent = `Distance: ${data.distanceCm} cm`;
    document.getElementById("fan-status").textContent = `Fan Status: ${data.fanStatus ? "ON" : "OFF"}`;
    document.getElementById("current-reading").textContent = `Current Consumption: ${data.current} A`;
    document.getElementById("electric-usage").textContent = `Electric Usage: ${data.electricUsageKWh} kWh`;

    // Update device status
    document.getElementById("status-text").textContent = "Connected and receiving data";
}

// Function to update the temperature and proximity thresholds on the server
async function updateThresholds() {
    const temperatureThreshold = parseFloat(document.getElementById("temp-threshold").value);
    const proximityThreshold = parseFloat(document.getElementById("proximity-threshold").value);

    const payload = {
        temperatureThreshold: temperatureThreshold,
        distanceThreshold: proximityThreshold
    };

    try {
        const response = await fetch("http://localhost:8080/api/update-fan-status", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            console.log("Thresholds updated successfully");
        } else {
            console.error("Error updating thresholds:", response.statusText);
        }
    } catch (error) {
        console.error("Error updating thresholds:", error);
    }
}

// Function to toggle temperature units between °C and °F
function toggleTemperatureUnit() {
    const tempElement = document.getElementById("temperature");
    const currentTemp = parseFloat(tempElement.textContent.split(":")[1].trim());

    // Toggle between °C and °F
    if (tempElement.textContent.includes("°C")) {
        const fahrenheit = (currentTemp * 9/5) + 32;
        tempElement.textContent = `Temperature: ${fahrenheit.toFixed(2)} °F`;
    } else {
        const celsius = (currentTemp - 32) * 5/9;
        tempElement.textContent = `Temperature: ${celsius.toFixed(2)} °C`;
    }
}

// Function to toggle distance units between cm and inches
function toggleDistanceUnit() {
    const distanceElement = document.getElementById("distance");
    const currentDistance = parseFloat(distanceElement.textContent.split(":")[1].trim());

    // Toggle between cm and inches
    if (distanceElement.textContent.includes("cm")) {
        const inches = currentDistance / 2.54;
        distanceElement.textContent = `Distance: ${inches.toFixed(2)} inches`;
    } else {
        const cm = currentDistance * 2.54;
        distanceElement.textContent = `Distance: ${cm.toFixed(2)} cm`;
    }
}
