<script setup>
import { onMounted, ref } from 'vue'
import L from 'leaflet'
import axios from 'axios'

const mapContainer = ref(null)
const itineraries = ref([])
const selectedItinerary = ref(null)
const notification = ref(null)

const showNotification = (message, type = 'info') => {
    notification.value = { message, type }
    setTimeout(() => {
        notification.value = null
    }, 3000)
}

onMounted(async () => {
  // Initialize Map with Dark Theme
  const map = L.map(mapContainer.value, {
      zoomControl: false,
      attributionControl: false
  }).setView([20, 0], 3)

  // Dark Matter Tiles
  L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
    subdomains: 'abcd',
    maxZoom: 20
  }).addTo(map)

  // Layer Group for Itineraries
  const itineraryLayerGroup = L.layerGroup().addTo(map)

  // Load World GeoJSON
  try {
      const geoResponse = await axios.get('https://raw.githubusercontent.com/johan/world.geo.json/master/countries.geo.json')
      L.geoJSON(geoResponse.data, {
          style: {
              color: '#4a90e2',
              weight: 1,
              opacity: 0.3,
              fillOpacity: 0.05,
              fillColor: '#4a90e2'
          },
          onEachFeature: (feature, layer) => {
              // Hover effects
              layer.on('mouseover', () => {
                  layer.setStyle({ fillOpacity: 0.3, weight: 2, color: '#6ab0ff' })
              })
              layer.on('mouseout', () => {
                  layer.setStyle({ fillOpacity: 0.05, weight: 1, color: '#4a90e2' })
              })

              layer.on('click', async () => {
                  const countryName = feature.properties.name
                  showNotification(`Updating leader info for ${countryName}...`, 'info')
                  
                  try {
                      await axios.post('http://localhost:8080/api/itineraries/update', { country: countryName })
                      showNotification(`Updated info for ${countryName}!`, 'success')
                      fetchItineraries()
                  } catch (err) {
                      console.error(err)
                      showNotification(`Failed to update info for ${countryName}`, 'error')
                  }
              })
          }
      }).addTo(map)
  } catch (e) {
      console.error("Failed to load GeoJSON", e)
  }

  // Fetch Data Function
  const fetchItineraries = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/itineraries')
        itineraries.value = response.data
        
        // Clear existing itinerary layers
        itineraryLayerGroup.clearLayers()

        itineraries.value.forEach(itinerary => {
            if (itinerary.startLat && itinerary.startLng && itinerary.endLat && itinerary.endLng) {
                const start = [itinerary.startLat, itinerary.startLng]
                const end = [itinerary.endLat, itinerary.endLng]
                
                // Draw Line with Animation Class
                const polyline = L.polyline([start, end], { 
                    color: '#ff4757', 
                    weight: 3, 
                    opacity: 0.8,
                    className: 'animated-line' 
                }).addTo(itineraryLayerGroup)
                
                // Custom Icon
                const createCustomIcon = (color) => L.divIcon({
                    className: 'custom-div-icon',
                    html: `<div style="background-color: ${color}; width: 12px; height: 12px; border-radius: 50%; border: 2px solid white; box-shadow: 0 0 8px ${color};"></div>`,
                    iconSize: [12, 12],
                    iconAnchor: [6, 6]
                })

                // Add Markers with Custom Icons
                L.marker(start, { icon: createCustomIcon('#2ed573') }).addTo(itineraryLayerGroup).bindPopup(`Start: ${itinerary.startLocation}`)
                L.marker(end, { icon: createCustomIcon('#ff4757') }).addTo(itineraryLayerGroup).bindPopup(`End: ${itinerary.endLocation}`)
                
                // Hover Event (Mouseover)
                polyline.on('mouseover', (e) => {
                    selectedItinerary.value = itinerary
                    L.popup({ className: 'dark-popup', closeButton: false })
                        .setLatLng(e.latlng)
                        .setContent(`
                            <div class="popup-content">
                                <h3>${itinerary.leader.name}</h3>
                                <div class="subtitle">${itinerary.leader.country}</div>
                                <div class="detail-row">
                                    <span class="label">Route:</span>
                                    <span>${itinerary.startLocation} → ${itinerary.endLocation}</span>
                                </div>
                                <div class="detail-row">
                                    <span class="label">Date:</span>
                                    <span>${new Date(itinerary.startDate).toLocaleDateString()} - ${new Date(itinerary.endDate).toLocaleDateString()}</span>
                                </div>
                                <div class="description">${itinerary.description}</div>
                            </div>
                        `)
                        .openOn(map)
                })

                // Optional: Close on mouseout, or keep until another is hovered
                // polyline.on('mouseout', () => {
                //    map.closePopup()
                // })
            }
        })
      } catch (error) {
        console.error("Failed to fetch itineraries", error)
      }
  }

  // Initial Fetch
  fetchItineraries()
})
</script>

<template>
  <div class="map-wrapper">
    <div ref="mapContainer" class="map-container"></div>
    
    <!-- Notification Toast -->
    <div v-if="notification" :class="['notification', notification.type]">
        {{ notification.message }}
    </div>

    <!-- Info Panel (Optional, can be removed if popup is enough, but keeping for now) -->
    <div v-if="selectedItinerary" class="info-panel">
        <h2>Trip Details</h2>
        <p><strong>Leader:</strong> {{ selectedItinerary.leader.name }}</p>
        <p><strong>Country:</strong> {{ selectedItinerary.leader.country }}</p>
        <p><strong>Route:</strong> {{ selectedItinerary.startLocation }} -> {{ selectedItinerary.endLocation }}</p>
        <p><strong>Description:</strong> {{ selectedItinerary.description }}</p>
    </div>
  </div>
</template>

<style scoped>
.map-wrapper {
    position: fixed;
    top: 0;
    left: 0;
    height: 100vh;
    width: 100vw;
    background-color: #1e1e1e;
}
.map-container {
    height: 100%;
    width: 100%;
    z-index: 1;
    background-color: #1e1e1e;
}

/* Notification Toast */
.notification {
    position: fixed;
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
    padding: 12px 24px;
    border-radius: 8px;
    color: white;
    font-weight: 500;
    z-index: 2000;
    box-shadow: 0 4px 12px rgba(0,0,0,0.3);
    animation: slideDown 0.3s ease-out;
}
.notification.info { background-color: #3742fa; }
.notification.success { background-color: #2ed573; }
.notification.error { background-color: #ff4757; }

@keyframes slideDown {
    from { top: -50px; opacity: 0; }
    to { top: 20px; opacity: 1; }
}

.info-panel {
    position: absolute;
    bottom: 30px;
    left: 30px;
    background: rgba(30, 30, 30, 0.9);
    color: #f1f2f6;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.4);
    z-index: 1000;
    max-width: 320px;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255,255,255,0.1);
}
.info-panel h2 {
    margin-top: 0;
    color: #2ed573;
    font-size: 1.2rem;
    margin-bottom: 15px;
}
.info-panel p {
    margin: 8px 0;
    font-size: 0.9rem;
    line-height: 1.4;
}
</style>

<style>
/* Global overrides for Leaflet in Dark Mode */
.leaflet-popup-content-wrapper, .leaflet-popup-tip {
    background: rgba(30, 30, 30, 0.95);
    color: #f1f2f6;
    box-shadow: 0 4px 20px rgba(0,0,0,0.4);
}
.leaflet-popup-content {
    margin: 15px;
}
.popup-content h3 {
    margin: 0 0 5px 0;
    color: #2ed573;
}
.popup-content .subtitle {
    color: #a4b0be;
    font-size: 0.9rem;
    margin-bottom: 10px;
}
.popup-content .detail-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 5px;
    font-size: 0.85rem;
}
.popup-content .label {
    font-weight: bold;
    color: #747d8c;
    margin-right: 10px;
}
.popup-content .description {
    margin-top: 10px;
    font-size: 0.85rem;
    line-height: 1.4;
    border-top: 1px solid rgba(255,255,255,0.1);
    padding-top: 10px;
}

/* Animated Line */
.animated-line {
    stroke-dasharray: 10;
    animation: dash 30s linear infinite;
}

@keyframes dash {
    to {
        stroke-dashoffset: -1000;
    }
}

.custom-div-icon {
    background: transparent;
    border: none;
}
</style>
