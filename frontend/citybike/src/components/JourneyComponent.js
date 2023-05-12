import axios from 'axios';
import React from 'react';

class JourneyComponent extends React.Component {


    constructor(props) {

        super(props)

        this.state = {

            journeys: [],

            currentPage: 1,

            recordsPerPage: 15, 
            
            totalPages: 0,

            sortBy: "id",

            sortParameters : {
                'Departure time': "departureDate",
                'Return time': "returnDate",
                'Departure station': "departureStation",
                'Return station': 'returnStation',
                'Distance': 'coveredDistance',
                'Duration': 'duration'
            }

        }
    }

    componentDidMount() {
        this.fetchJourneysByPage(this.state.currentPage);
        this.fetchTotalPages(this.state.recordsPerPage); 
    }


    fetchTotalPages(recordsPerPage){
        axios.get("http://localhost:8080/journeys/count")
        .then(response => response.data).then((data) => {
            this.setState({
                totalPages: Math.ceil(data / recordsPerPage)

            })
        })
    }

    showLastPage = () => {
        
        if(this.state.currentPage >= this.state.totalPages){
            return;
        }

        this.setState({
            currentPage: this.state.totalPages
        }) 

        this.fetchJourneysByPage(this.state.totalPages)

    }

    showFirstPage = () => {
        
        let firstPage = 1;

        if(this.state.currentPage < firstPage){
            return
        }
            
        this.setState({
            currentPage: firstPage
        })

        this.fetchJourneysByPage(firstPage)

    }

    showNextPage = () => {
        
        if(this.state.currentPage >= this.state.totalPages){
            return;
        }

        const newPage = this.state.currentPage + 1;

        this.setState({
            currentPage: newPage
        })

        this.fetchJourneysByPage(newPage);

    }

    changeSortingParameter = (event) => {
        
        this.setState({
            sortBy: event.target.value
        })

        this.fetchJourneysByPage(1);
    }

    showPreviousPage = () => {

        if(this.state.currentPage <= 1){
            return;
        }

        const newPage = this.state.currentPage - 1;

        this.setState({
            currentPage: newPage
        })

        this.fetchJourneysByPage(newPage);

    }


    fetchJourneysByPage(pageNumber) {

        pageNumber -= 1

        axios.get("http://localhost:8080/journeys?pageNumber=" + pageNumber
            + "&pageSize=" + this.state.recordsPerPage + "&sortBy=" + this.state.sortBy)
            .then(response => response.data).then((data) => {
                this.setState({
                    journeys: data

                })
            })
    }

    render() {
        const { journeys, currentPage, recordsPerPage, totalPages } = this.state;

        const sortMap = this.state.sortParameters;
        
        return (
            <div>

                <h1 className="text-center mt-5 ">All bike journeys</h1>
                <div className="container mt-2">
                    <table className="table table-bordered border-info shadow">
                        <thead>
                            <tr>
                                <th>number</th>
                                <th>Departure time</th>
                                <th>Return time</th>
                                <th>Departure station</th>
                                <th>Return station</th>
                                <th>Journey distance</th>
                                <th>Journey duration</th>
                            </tr>
                        </thead>
                        <tbody>
                            {journeys.length === 0 ?
                                <tr align="center"><td colSpan="5">No journeys found</td></tr> :
                                journeys.map(
                                    (journey, index) => (

                                        <tr key={journey.id}>
                                            <td>{(recordsPerPage * (currentPage - 1)) + index + 1}</td>
                                            <td>{journey.departureDate}</td>
                                            <td>{journey.returnDate}</td>
                                            <td>{journey.departureStation.stationName}</td>
                                            <td>{journey.returnStation.stationName}</td>
                                            <td>{journey.coveredDistance}</td>
                                            <td>{journey.duration}</td>

                                        </tr>
                                    )
                                )
                            }
                        </tbody>
                    </table>
                    <table className="table">
                        <div style={{ float: 'left', fontFamily: 'monospace', color: '#0275d8' }}>
                            Page {currentPage} of {totalPages}
                        </div>
                        <div style={{ float: 'right' }}>
                            <div class="clearfix"></div>
                            <nav aria-label="Page navigation example">
                                <ul class="pagination">
                                    <li class="page-item"><a type="button" class="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showPreviousPage}>Previous</a></li>
                                    <li class="page-item"><a type="button" class="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showFirstPage}>First</a></li>
                                    <li class="page-item"><a type="button" class="page-link" disabled={currentPage === totalPages ? true : false} onClick={this.showNextPage}>Next</a></li>
                                    <li class="page-item"><a type="button" class="page-link" disabled={currentPage === totalPages ? true : false} onClick={this.showLastPage}>Last</a></li>
                                </ul>
                            </nav>
                        </div>
                        
                        <div>
                            <select onChange={this.changeSortingParameter}>
                            <option>Sort By</option>
                            {Object.entries(sortMap).map(([key, value]) => (
                                <option value = {value}>{key}</option>
                            ))}
                            </select>
                        </div>

                    </table>
                </div>
            </div>
        )
    }
}

export default JourneyComponent;