'''
Created on Feb 1, 2010

@author: tim
for cs44 w10

X always goes first
'''
import re

class State(object):
    """This is a Game State"""

    def __init__(self):
        """Constructor"""
        self._state = ["......."] * 6; # number of rows
        self._moves = 0 #number of moves so far in game
        self._build_diagonal_set()

        
    def encode(self): #why did afra name this encode...
        """returns a string representation of the _state"""
        s = "-1 2 3 4 5 6 7-\n"
        for row in self._state:
            s = s + "|" + " ".join(row) + "|\n"
        s = s + "-1 2 3 4 5 6 7-"
        return s
            
    def decode(self, str):
        """takes a string input and constructs this object with it"""
        i = 0
        for s in str.split("\n"):
            if re.match("^[|].*[|]$", s):
                self._state[i] = s.split("|")[1]
                i = i+1
                
        self._moves = self._count_turns() #set the next turn correctly
                        
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
        m = m - 1 #make start from 0
        rowcount = 0 #start from top but count as if from bottom
        if int(m + 1) in self._legal_moves(): #check against human list
            
            #figure out the first non free pos
            rowcount = self._first_free(m)
                        
            #then do the move
            tmp = self._state[rowcount - 1]
            self._state[rowcount - 1] = tmp[0:m] + self._next_move() + tmp[m + 1:] #stupid immutable strings
            self._moves = self._moves + 1  #increment move count
            return True
            
        else:
            print "not a legal move buddy"
            return False
        
        
    def undo_move(self, m):
        """undoes a move"""
        m = m - 1 #make start from 0
        
        #figure out the first non free pos
        rowcount = self._first_free(m)
                            
        #then undo the move if we are allowed to
        if rowcount < 6:
            tmp = self._state[rowcount]
            self._state[rowcount] = tmp[0:m] + '.' + tmp[m + 1:] #stupid immutable strings
            self._moves = self._moves - 1  #decrement the move count
            return True
        else:
            print "not a legal undo buddy"
            return False

    
    def _next_move(self):
        """figures out who would make the next move"""
        if self._moves % 2 == 0:
            return 'X'
        else:
            return 'O'
    
    def _count_turns(self):
        """figures out how many moves have been so far"""
        i = 0
        for column in self._state:
            i = i + column.count('X')
            i = i + column.count('O')
        return i
    
    
    def _first_free(self, m):
        """figure out the first non free position in col m counting down from top"""
        rowcount = 0

        for col in self._state:
            if col[m] == '.':
                rowcount = rowcount + 1
            else:
                break
        return rowcount
    
    
    def is_win(self):
        """returns tuple (bool, player) , true if current state is a win"""
        list = self.build_segments()
        return self._is_win(list)
    
    
    def _is_win(self, list):
        """takes list returns tuple (bool, player) , true if current state is a win"""
        
        for s in list:
            if s == "XXXX":
                return (True, "X")
            elif s == "OOOO":
                return (True, "O")
            
        return (False, "better luck next time")
            
    
    def _build_diagonal_set(self):
        """builds a list of lists of all possible coordinates of diagonals"""
        self.allsegs = []
        for y in range(0, 6):
            for x in range(0, 7):
                # - - 
                if x > 2 and y > 2:
                    self.allsegs.append([(x,y),(x-1,y-1),(x-2,y-2),(x-3,y-3)])
                # - +
                if x > 2 and y < 3:
                    self.allsegs.append([(x,y),(x-1,y+1),(x-2,y+2),(x-3,y+3)])
        
        
    def build_segments(self):
        """builds a set of all size 4 segments on the board"""
        
        #all vertical segments
        columns = map(lambda *row: list(row), *self._state) #transpose to look in columns
        columns2 =["".join(s) for s in columns]
        columns3 = []
        for c in columns2:  #get lengths of 4 segments
            for i in range(0,3):
                columns3.append(c[0+i:4+i])
        
        #all horizontal segments
        rows = []
        for r in self._state:
            for i in range(0,4):
                rows.append(r[0+i:4+i])
            
        #all diagonal segments
        diags = [] 

        #build diag strings
        for seg in self.allsegs:
            st = ""
            for pos in seg:
                (x,y) = pos
                st = st + self._state[y][x]
            diags.append(st)
            

        #build up mega list of all possible segments
        l = []
        l.extend(rows)
        l.extend(columns3)
        l.extend(diags)
        return l
                                
        