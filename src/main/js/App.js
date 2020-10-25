import React, { Component } from "react";
import ReactDOM from "react-dom";
import {Form,Button,Menu} from 'react-tradeshift-ui';
import ChangeParent from './ChangeParent';
import Descendants from './Descendants';
import NewStructure from './NewStructure';
import axios from 'axios';
import Tree from 'react-tree-graph';
import 'react-tree-graph/dist/style.css'
import './main.css';



class Page extends Component {

	 constructor(props) {
		 super(props);
		 this.state = { action:'none', treeData:{} };
		 this.handleGetStructure = this.handleGetStructure.bind(this);
		 this.handleChangeParent = this.handleChangeParent.bind(this);
		 this.show = this.show.bind(this);
		 this.hide = this.hide.bind(this);
		 this.handleShowDescendants = this.handleShowDescendants.bind(this);
		 this.handleNewStructure = this.handleNewStructure.bind(this);
	}
	handleGetStructure(){
		axios.get('/api/tree').
		then(result=>this.setState(prevState =>{
			  return{
			       ...prevState,
			       treeData: result.data
			   }
		}))
		.catch(err => {
			alert(err.response.data.errorMessage);
		});
		
	}
	show(value){
		this.setState(prevState =>{
			  return{
			       ...prevState,
			       action: value
			   }
		});
		
	}
	hide(){
		this.setState(prevState =>{
			  return{
			       ...prevState,
			       action: 'none'
			   }
		});
	}
	handleChangeParent(id,parentId){
		axios.put('/api/tree/'+id+ '/move/'+parentId )
		.then(result=>this.handleGetStructure())
		.catch(err => {
			alert(err.response.data.errorMessage);
		});
		
	}
	handleShowDescendants(id){
		axios.get('/api/tree/'+id+ '/structure/').
		then(result=>this.setState(prevState =>{
			  return{
			       ...prevState,
			       treeData: result.data
			   }
		}))
		.catch(err => {
			alert(err.response.data.errorMessage);
		});
		
	}
	handleNewStructure(tree){
		axios.post('/api/tree/',tree,  {
            headers: { 'Content-Type': 'application/json' }})
          .then(result=>this.handleGetStructure())
          .catch(err => {
        	  alert(err.response.data.errorMessage);
		});
		
	}
    render() {
    	 return (
    			 <div  className="container">
    			
    			 <div className="aside">
    			 <Menu>
    				<li>
    					<button onClick={this.handleGetStructure}>
    						<span>Show The Structure!</span>
    					</button>
    				</li>
    				<li>
					<button onClick={()=>this.show('changeParent')}>
						<span>Change Parent</span>
					</button>
					</li>
					<li>
					<button onClick={()=>this.show('descendants')}>
						<span>Decsendants</span>
					</button>
					</li>
					<li>
						<button onClick={()=>this.show('newStructure')}>
							<span> New Structure</span>
						</button>
					</li>
    				
    			</Menu>
				</div>
				<div className = 'column'>
				{ this.state.action=='changeParent' ? ( <ChangeParent cancel={this.hide} action={this.handleChangeParent}/>) :null}
				{ this.state.action=='descendants' ? ( <Descendants cancel={this.hide} action={this.handleShowDescendants}/>) :null}
				{ this.state.action=='newStructure' ? ( <NewStructure cancel={this.hide} action={this.handleNewStructure}/>) :null}
				
				
				</div>
				<div className = 'column'>
				<Tree
					data={this.state.treeData}
					height={600}
					width={750}
					nodeRadius={10}
					animated={true}
				/>
				</div>
				</div>
                 )
    }

}

ReactDOM.render(
    <Page />,
    document.getElementById('react')
);