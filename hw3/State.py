'''
Created on Feb 1, 2010

@author: tim
'''
import re

class State(object):
    """This is a Game State"""

    def __init__(self):
        """Constructor"""
        self._state = [""] * 6; # number of rows
        self._turn = 'X' #whos turn it is X or O #TODO: should we be keeping tack of this

        
    def encode(self):
        """returns a string representation of the _state"""
        s = "-1234567-\n"
        for row in self._state:
            s = s + "|" + row + "|\n"
        s = s + "-1234567-"
        return s
            
    def decode(self, str):
        """takes a string input and constructs this object with it"""
        i = 0
        for s in str.split("\n"):
            if re.match("^[|].*[|]$", s):
                self._state[i] = s.split("|")[1]
                i = i+1
                
        self._turn = self._whos_turn() #set the next turn correctly
                        
    def legal_moves(self):
        """returns a string of legal moves there are from this _state"""
        return " ".join([str(int) for int in self._legal_moves()])
    
    
    def _legal_moves(self):
        """returns a list of legal moves there are from this _state"""
        l = []
        for index, column in enumerate(self._state[0]): #grab the topmost column
            if column == ".":
                l.append(index + 1) #use human column count
        return l
             
                
    def do_move(self, m):
        """does a move"""
        #TODO: remember to increment turn # maybe simply keep a counter or even just reconstruct every time
        if int(m) in self._legal_moves():
            pass
        
        
    def undo_move(self, m):
        """undoes a move"""
        #TODO: remember to swamp turns
        pass
    
    
    
    def _whos_turn(self):
        """figures out who's turn it is next and returns"""
        xs = 0
        os = 0
        for column in self._state:
            xs = xs + column.count('X')
            os = os + column.count('O')
        
        if xs <= os:
            return 'X'
        else:
            return 'O'
    
                    
    def _test(self):
        """tests some stuff"""
        s = """
-1234567-
|.......|
|.......|
|.......|
|.......|
|..O....|
|.OXXXO.|
-1234567-"""
        self.decode(s)
        print self.legal_moves()
        print self._whos_turn()
        
            
            
            
            
        
        