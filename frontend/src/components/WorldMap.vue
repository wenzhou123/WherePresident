<script setup>
import { onMounted, ref, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'

const mapContainer = ref(null)
const notification = ref(null)
let chartInstance = null

const showNotification = (message, type = 'info') => {
    notification.value = { message, type }
    setTimeout(() => {
        notification.value = null
    }, 3000)
}

onMounted(async () => {
  // Initialize ECharts instance
  chartInstance = echarts.init(mapContainer.value)

  // Show loading state
  chartInstance.showLoading({
    maskColor: 'rgba(30, 30, 30, 0.8)',
    textColor: '#fff'
  })

  try {
      // Load World GeoJSON
      const geoResponse = await axios.get('https://raw.githubusercontent.com/johan/world.geo.json/master/countries.geo.json')
      echarts.registerMap('world', geoResponse.data)
      
      chartInstance.hideLoading()

      // Initial Option
      const option = {
          backgroundColor: '#1e1e1e',
          title: {
              text: 'Global Leader Itineraries',
              left: 'center',
              textStyle: {
                  color: '#fff'
              }
          },
          tooltip: {
              trigger: 'item',
              backgroundColor: 'rgba(30, 30, 30, 0.9)',
              borderColor: '#777',
              textStyle: {
                  color: '#fff'
              },
              formatter: function (params) {
                  if (params.seriesType === 'lines') {
                      const data = params.data;
                      return `
                          <div style="padding: 5px;">
                              <h3 style="color: #2ed573; margin: 0 0 5px 0;">${data.leaderName}</h3>
                              <div style="color: #a4b0be; font-size: 12px; margin-bottom: 5px;">${data.leaderCountry}</div>
                              <div>${data.fromName} -> ${data.toName}</div>
                              <div style="margin-top: 5px; font-size: 12px; color: #ccc;">${data.description}</div>
                          </div>
                      `;
                  } else if (params.seriesType === 'effectScatter') {
                      return `${params.data.name}`;
                  } else {
                      return params.name;
                  }
              }
          },
          geo: {
              map: 'world',
              roam: true,
              label: {
                  emphasis: {
                      show: false
                  }
              },
              itemStyle: {
                  normal: {
                      areaColor: '#323c48',
                      borderColor: '#111'
                  },
                  emphasis: {
                      areaColor: '#2a333d'
                  }
              }
          },
          series: []
      };

      chartInstance.setOption(option);

      // Handle Click on Country
      chartInstance.on('click', async (params) => {
          if (params.componentType === 'geo' || params.seriesType === 'map') {
              const countryName = params.name
              showNotification(`Updating leader info for ${countryName}...`, 'info')
              
              try {
                  await axios.post('/api/itineraries/update', { country: countryName })
                  showNotification(`Updated info for ${countryName}!`, 'success')
                  fetchItineraries()
              } catch (err) {
                  console.error(err)
                  showNotification(`Failed to update info for ${countryName}`, 'error')
              }
          }
      });

      // Fetch Data
      fetchItineraries()

  } catch (e) {
      console.error("Failed to load GeoJSON or init map", e)
      chartInstance.hideLoading()
  }

  // Handle Resize
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
    window.removeEventListener('resize', handleResize)
    if (chartInstance) {
        chartInstance.dispose()
    }
})

const handleResize = () => {
    if (chartInstance) {
        chartInstance.resize()
    }
}

const fetchItineraries = async () => {
    try {
        const response = await axios.get('/api/itineraries')
        const itineraries = response.data
        
        const linesData = []
        const scatterData = []

        itineraries.forEach(itinerary => {
            if (itinerary.startLat && itinerary.startLng && itinerary.endLat && itinerary.endLng) {
                const startPoint = [itinerary.startLng, itinerary.startLat];
                const endPoint = [itinerary.endLng, itinerary.endLat];
                
                // Line Data
                linesData.push({
                    coords: [startPoint, endPoint],
                    fromName: itinerary.startLocation,
                    toName: itinerary.endLocation,
                    leaderName: itinerary.leader.name,
                    leaderCountry: itinerary.leader.country,
                    description: itinerary.description,
                    lineStyle: {
                        color: '#ff4757'
                    }
                });

                // Scatter Data (Start and End)
                scatterData.push({
                    name: itinerary.startLocation,
                    value: startPoint.concat(10), // value includes lat, lng, and size/value
                    itemStyle: { color: '#2ed573' }
                });
                scatterData.push({
                    name: itinerary.endLocation,
                    value: endPoint.concat(10),
                    itemStyle: { color: '#ff4757' }
                });
            }
        })

        chartInstance.setOption({
            series: [
                {
                    name: 'Routes',
                    type: 'lines',
                    zlevel: 1,
                    effect: {
                        show: true,
                        period: 6,
                        trailLength: 0.7,
                        color: '#fff',
                        symbolSize: 3
                    },
                    lineStyle: {
                        normal: {
                            color: '#a6c84c',
                            width: 0,
                            curveness: 0.2
                        }
                    },
                    data: linesData
                },
                {
                    name: 'Routes Line',
                    type: 'lines',
                    zlevel: 2,
                    symbol: ['none', 'arrow'],
                    symbolSize: 10,
                    effect: {
                        show: true,
                        period: 6,
                        trailLength: 0,
                        symbol: 'plane',
                        symbolSize: 15
                    },
                    lineStyle: {
                        normal: {
                            color: '#a6c84c',
                            width: 1,
                            opacity: 0.6,
                            curveness: 0.2
                        }
                    },
                    data: linesData
                },
                {
                    name: 'Locations',
                    type: 'effectScatter',
                    coordinateSystem: 'geo',
                    zlevel: 2,
                    rippleEffect: {
                        brushType: 'stroke'
                    },
                    label: {
                        normal: {
                            show: true,
                            position: 'right',
                            formatter: '{b}'
                        }
                    },
                    symbolSize: function (val) {
                        return val[2] / 2; // Dynamic size based on value
                    },
                    itemStyle: {
                        normal: {
                            color: '#f4e925'
                        }
                    },
                    data: scatterData
                }
            ]
        })

    } catch (error) {
        console.error("Failed to fetch itineraries", error)
    }
}
</script>

<template>
  <div class="map-wrapper">
    <div ref="mapContainer" class="map-container"></div>
    
    <!-- Notification Toast -->
    <div v-if="notification" :class="['notification', notification.type]">
        {{ notification.message }}
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
</style>
