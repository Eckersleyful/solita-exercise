import './App.css';
import JourneyComponent from './components/JourneyComponent'
import Tabs from './components/Tabs';
import StationComponent from './components/StationComponent'
function App() {
  return (
    <div className="App">
          <Tabs>
        <div label="Journeys">
          <JourneyComponent></JourneyComponent>
        </div>
        <div label="Stations">
          <StationComponent></StationComponent>
        </div>
      </Tabs>
            
    </div>
  );
}
 
export default App;
