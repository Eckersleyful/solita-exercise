import { fetchStationCount, fetchStationsByPage } from '../services/stationServices';
import React from 'react';


class StationComponent extends React.Component {


    constructor(props) {

        super(props)

        this.state = {

            stations: [],
            
            //Current pagination page
            pageNumber: 1,
            
            //How many rows we show at a time
            pageSize: 30,
            
            //How many stations there are in total in the DB
            stationCount: 0,
        }

        this.showNextPage = this.showNextPage.bind(this);
        this.showPreviousPage = this.showPreviousPage.bind(this);
        this.showLastPage = this.showLastPage.bind(this);
        this.showFirstPage = this.showFirstPage.bind(this);
    }

    async componentDidMount() {


        //Fetch initial data and set it as state
        var [stationCount, stations] = await Promise.all([
            fetchStationCount(),
            fetchStationsByPage(this.state.pageNumber, this.state.pageSize)
        ])

        this.setState({
            stations: stations.data,
            stationCount: stationCount.data

        })
    } 
    
    getTotalPages(stationCount = this.state.stationCount, recordsPerPage = this.state.pageSize){
        return Math.ceil(stationCount / recordsPerPage);
    }

    async showLastPage() {
        
        const totalPages = this.getTotalPages();

        if(this.state.pageNumber >= totalPages){
            return;
        }

        this.setState({
            pageNumber: totalPages
        }) 
        this.setStationData(totalPages, this.state.pageSize, this);


    }

    async showFirstPage() {
        
        let firstPage = 1;

        if(this.state.pageNumber < firstPage){
            return
        }
            
        this.setState({
            pageNumber: firstPage
        })

        this.setStationData(firstPage, this.state.pageSize, this);


    }

    async showNextPage() {

        console.log(this)

        if(this.state.pageNumber >= this.getTotalPages()){
            console.log("here")
            return;
        }

        const newPage = this.state.pageNumber + 1;

        this.setState({
            pageNumber: newPage
        })

        this.setStationData(newPage, this.state.pageSize, this);
    }


    async showPreviousPage() {
        
        if(this.state.pageNumber <= 1){
            return;
        }

        const newPage = this.state.pageNumber - 1;

        this.setState({
            pageNumber: newPage
        })

        this.setStationData(newPage, this.state.pageSize, this);
    }

    async setStationData(page, pageSize, state){
        const stations = await fetchStationsByPage(page, pageSize);

        state.setState({
            stations: stations.data
        })
    }

    render() {
        const { stations, pageNumber: currentPage, pageSize: recordsPerPage} = this.state;

        const totalPages = this.getTotalPages();
        return (
            <div className = "main-div">

                <div className = "center-div">
                    <h1>Helsinki Citybike journeys</h1>
                </div>
                
                <div className = "data-table-wrapper center-div">
                    <table className = "data-table">
                        <thead id = "table-header">
                            <tr>
                                <th>Index</th>
                                <th>Station name</th>
                                <th>Departure count</th>
                                <th>Return count</th>
                            </tr>
                        </thead>
                        <tbody>
                            {stations.length === 0 ?
                                <tr>
                                    <td>No stations found</td>
                                </tr> :
                                stations.map(
                                    (station, index) => (
                                        <tr key = {station.id + "meow"}>
                                            <td>{(recordsPerPage * (currentPage - 1)) + index + 1}</td>
                                            <td>{station.stationName}</td>
                                            <td>{station.departingJourneysCount}</td>
                                            <td>{station.returningJourneysCount}</td>
                                        </tr>
                                    )
                                )
                            }
                        </tbody>
                    </table>
                </div>
                <div className = "center-div pagination-parent">
                    <div style={{ float: 'left', fontFamily: 'monospace', color: '#0275d8' }}>
                        <p>Page {currentPage} of {totalPages}</p> 
                    </div>
                    <div id = "station-navigation-parent" className = "navigation-buttons-parent">
                        <nav aria-label="Page navigation">
                            <ul className="pagination-list">
                                <li className="page-item"><a type="button" className="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showPreviousPage}>Previous</a></li>
                                <li className="page-item"><a type="button" className="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showFirstPage}>First</a></li>
                                <li className="page-item"><a type="button" className="page-link" disabled={currentPage === totalPages ? true : false} onClick={this.showNextPage}>Next</a></li>
                                <li className="page-item"><a type="button" className="page-link" disabled={currentPage === totalPages ? true : false} onClick={this.showLastPage}>Last</a></li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        )
    }
}

export default StationComponent;