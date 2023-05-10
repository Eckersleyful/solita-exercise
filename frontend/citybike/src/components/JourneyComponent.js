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

            sortBy: "id"

        }
    }

    componentDidMount() {
        this.fetchJourneysByPage(this.state.currentPage);
        console.log(this.state)
        this.fetchTotalPages(this.state.recordsPerPage); 
    }

    fetchTotalPages(recordsPerPage){
        axios.get("http://localhost:8080/journeys/count")
        .then(response => response.data).then((data) => {
            this.setState({
                totalPages: Math.round(data / recordsPerPage)

            })
        })
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
        console.log(this.state)
        const { journeys, currentPage, recordsPerPage, totalPages } = this.state;
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
                                    <li class="page-item"><a type="button" class="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showPrevPage}>Previous</a></li>
                                    <li class="page-item"><a type="button" class="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showFirstPage}>First</a></li>
                                    <li class="page-item"><a type="button" class="page-link" disabled={currentPage === totalPages ? true : false} onClick={this.showNextPage}>Next</a></li>
                                    <li class="page-item"><a type="button" class="page-link" disabled={currentPage === totalPages ? true : false} onClick={this.showLastPage}>Last</a></li>
                                </ul>
                            </nav>
                        </div>
                    </table>
                </div>
            </div>
        )
    }
}

export default JourneyComponent;