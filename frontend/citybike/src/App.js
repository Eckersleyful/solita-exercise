import './App.css';
import JourneyComponent from './components/JourneyComponent'
import Tabs from './components/Tabs';
import StationComponent from './components/StationComponent'
function App() {
  return (
    <div className="App">
      <div className= "center-div">
          <Tabs>
        <div label="Journeys">
          <JourneyComponent></JourneyComponent>
        </div>
        <div label="Stations">
          <StationComponent></StationComponent>
        </div>
      </Tabs>
      </div> 
    </div>
  );
}
 
export default App;
